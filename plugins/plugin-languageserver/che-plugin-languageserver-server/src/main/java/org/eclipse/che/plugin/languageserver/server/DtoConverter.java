/*
 * *****************************************************************************
 *  Copyright (c) 2012-2016 Codenvy, S.A.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Codenvy, S.A. - initial API and implementation
 * *****************************************************************************
 */
package org.eclipse.che.plugin.languageserver.server;

import io.typefox.lsapi.CodeLensOptions;
import io.typefox.lsapi.CompletionOptions;
import io.typefox.lsapi.DocumentOnTypeFormattingOptions;
import io.typefox.lsapi.InitializeResult;
import io.typefox.lsapi.LanguageDescription;
import io.typefox.lsapi.ServerCapabilities;
import io.typefox.lsapi.SignatureHelpOptions;

import org.eclipse.che.plugin.languageserver.shared.lsapi.CodeLensOptionsDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.CompletionOptionsDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.DocumentOnTypeFormattingOptionsDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.InitializeResultDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.LanguageDescriptionDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.ServerCapabilitiesDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.SignatureHelpOptionsDTO;

import java.util.List;
import java.util.stream.Collectors;

import static org.eclipse.che.dto.server.DtoFactory.newDto;

/**
 * @author Anatoliy Bazko
 */
public class DtoConverter {

    public static InitializeResultDTO asDto(InitializeResult initializeResult) {
        List<LanguageDescriptionDTO> languageDescriptionDTOs = initializeResult.getSupportedLanguages()
                                                                               .stream()
                                                                               .map(DtoConverter::asDto)
                                                                               .collect(Collectors.toList());

        InitializeResultDTO initializeResultDTO = newDto(InitializeResultDTO.class);
        initializeResultDTO.setCapabilities(asDto(initializeResult.getCapabilities()));
        initializeResultDTO.setSupportedLanguages(languageDescriptionDTOs);
        return initializeResultDTO;
    }

    public static LanguageDescriptionDTO asDto(LanguageDescription languageDescription) {
        LanguageDescriptionDTO languageDescriptionDTO = newDto(LanguageDescriptionDTO.class);
        languageDescriptionDTO.setFileExtensions(languageDescription.getFileExtensions());
        languageDescriptionDTO.setHighlightingConfiguration(languageDescription.getHighlightingConfiguration());
        languageDescriptionDTO.setLanguageId(languageDescription.getLanguageId());
        languageDescriptionDTO.setMimeTypes(languageDescription.getMimeTypes());
        return languageDescriptionDTO;
    }

    public static ServerCapabilitiesDTO asDto(ServerCapabilities capabilities) {
        ServerCapabilitiesDTO serverCapabilitiesDTO = newDto(ServerCapabilitiesDTO.class);

        serverCapabilitiesDTO.setCodeActionProvider(capabilities.isCodeActionProvider());

        CodeLensOptions codeLensProvider = capabilities.getCodeLensProvider();
        serverCapabilitiesDTO.setCodeLensProvider(codeLensProvider == null
                                                  ? null
                                                  : asDto(codeLensProvider));

        CompletionOptions completionProvider = capabilities.getCompletionProvider();
        serverCapabilitiesDTO.setCompletionProvider(completionProvider == null
                                                    ? null
                                                    : asDto(completionProvider));

        serverCapabilitiesDTO.setDefinitionProvider(capabilities.isDefinitionProvider());
        serverCapabilitiesDTO.setDocumentFormattingProvider(capabilities.isDocumentFormattingProvider());

        DocumentOnTypeFormattingOptions documentOnTypeFormattingProvider = capabilities.getDocumentOnTypeFormattingProvider();
        serverCapabilitiesDTO.setDocumentOnTypeFormattingProvider(documentOnTypeFormattingProvider == null
                                                                  ? null
                                                                  : asDto(documentOnTypeFormattingProvider));

        serverCapabilitiesDTO.setDocumentHighlightProvider(capabilities.isDocumentHighlightProvider());
        serverCapabilitiesDTO.setDocumentRangeFormattingProvider(capabilities.isDocumentRangeFormattingProvider());
        serverCapabilitiesDTO.setDocumentSymbolProvider(capabilities.isDocumentSymbolProvider());
        serverCapabilitiesDTO.setHoverProvider(capabilities.isHoverProvider());
        serverCapabilitiesDTO.setReferencesProvider(capabilities.isReferencesProvider());
        serverCapabilitiesDTO.setRenameProvider(capabilities.isRenameProvider());

        SignatureHelpOptions signatureHelpProvider = capabilities.getSignatureHelpProvider();
        serverCapabilitiesDTO.setSignatureHelpProvider(signatureHelpProvider == null
                                                       ? null
                                                       : asDto(signatureHelpProvider));

        serverCapabilitiesDTO.setTextDocumentSync(capabilities.getTextDocumentSync());
        serverCapabilitiesDTO.setWorkspaceSymbolProvider(capabilities.isWorkspaceSymbolProvider());

        return serverCapabilitiesDTO;
    }

    public static SignatureHelpOptionsDTO asDto(SignatureHelpOptions signatureHelpOptions) {
        SignatureHelpOptionsDTO signatureHelpOptionsDTO = newDto(SignatureHelpOptionsDTO.class);
        signatureHelpOptionsDTO.setTriggerCharacters(signatureHelpOptions.getTriggerCharacters());
        return signatureHelpOptionsDTO;
    }

    public static DocumentOnTypeFormattingOptionsDTO asDto(DocumentOnTypeFormattingOptions documentOnTypeFormattingOptions) {
        DocumentOnTypeFormattingOptionsDTO formattingOptionsDTO = newDto(DocumentOnTypeFormattingOptionsDTO.class);
        formattingOptionsDTO.setFirstTriggerCharacter(documentOnTypeFormattingOptions.getFirstTriggerCharacter());
        formattingOptionsDTO.setMoreTriggerCharacter(documentOnTypeFormattingOptions.getMoreTriggerCharacter());
        return formattingOptionsDTO;
    }

    public static CompletionOptionsDTO asDto(CompletionOptions completionOptions) {
        CompletionOptionsDTO completionOptionsDTO = newDto(CompletionOptionsDTO.class);
        completionOptionsDTO.setTriggerCharacters(completionOptions.getTriggerCharacters());
        completionOptionsDTO.setResolveProvider(completionOptions.getResolveProvider());
        return completionOptionsDTO;
    }

    public static CodeLensOptionsDTO asDto(CodeLensOptions codeLensOptions) {
        CodeLensOptionsDTO codeLensOptionsDTO = newDto(CodeLensOptionsDTO.class);
        codeLensOptionsDTO.setResolveProvider(codeLensOptions.getResolveProvider());
        return codeLensOptionsDTO;
    }

    private DtoConverter() { }

}
