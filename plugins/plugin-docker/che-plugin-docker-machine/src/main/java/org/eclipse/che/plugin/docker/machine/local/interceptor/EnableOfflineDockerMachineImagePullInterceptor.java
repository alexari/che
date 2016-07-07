/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.plugin.docker.machine.local.interceptor;

import com.google.common.base.MoreObjects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.che.api.core.model.machine.MachineConfig;
import org.eclipse.che.api.machine.server.exception.MachineException;
import org.eclipse.che.plugin.docker.client.DockerConnector;
import org.eclipse.che.plugin.docker.client.DockerFileException;
import org.eclipse.che.plugin.docker.client.ProgressMonitor;
import org.eclipse.che.plugin.docker.client.UserSpecificDockerRegistryCredentialsProvider;
import org.eclipse.che.plugin.docker.client.params.PullParams;
import org.eclipse.che.plugin.docker.client.parser.DockerImageIdentifier;
import org.eclipse.che.plugin.docker.client.parser.DockerImageIdentifierParser;
import org.eclipse.che.plugin.docker.machine.DockerInstanceProvider;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Allow creation of docker machine when image for machine cached but network is gone.
 * In that case we use cached image, otherwise pull the latest one.
 *
 * @author Alexander Garagatyi
 *
 * @see DockerInstanceProvider#pullImage(MachineConfig, String, ProgressMonitor)
 */
// todo add tests
public class EnableOfflineDockerMachineImagePullInterceptor implements MethodInterceptor {
    @Inject
    DockerConnector                               dockerConnector;
    @Inject
    UserSpecificDockerRegistryCredentialsProvider dockerCredentials;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        MachineConfig machineConfig = (MachineConfig)methodInvocation.getArguments()[0];
        ProgressMonitor progressMonitor = (ProgressMonitor)methodInvocation.getArguments()[2];

        try {
            pullImage(machineConfig, progressMonitor);
        } catch (IOException | MachineException | DockerFileException ignored) {
        }

        return methodInvocation.proceed();
    }

    private void pullImage(MachineConfig machineConfig, ProgressMonitor progressMonitor)
            throws IOException, MachineException, DockerFileException {

        String imageToPull = machineConfig.getSource().getLocation();
        if (imageToPull == null) {
            // Image is null, we can't pull it. Intercepted method should handle that case
            return;
        }

        DockerImageIdentifier imageIdentifier = DockerImageIdentifierParser.parse(imageToPull);

        dockerConnector.pull(PullParams.create(imageIdentifier.getRepository())
                                       .withTag(MoreObjects.firstNonNull(imageIdentifier.getTag(), "latest"))
                                       .withRegistry(imageIdentifier.getRegistry())
                                       .withAuthConfigs(dockerCredentials.getCredentials()),
                             progressMonitor);
    }
}
