package io.github.ctorressoftware.infrastructure.persistence.adapter;

import io.github.ctorressoftware.application.port.out.ProviderConfig;
import io.github.ctorressoftware.application.port.out.ProviderConfigRepository;

public class FileProviderConfigRepositoryAdapter implements ProviderConfigRepository {

    // private final CredentialsStorageManager credentialsStorageManager;

    public FileProviderConfigRepositoryAdapter() {
        throw new UnsupportedOperationException("Unsupported operation yet");
    }

    @Override
    public void save(ProviderConfig credentials) {
        // TODO: complete this method
    }

    @Override
    public <T extends ProviderConfig> T findByDomainAndAccount(String domain, String account, Class<T> configType) {
        return null;
    }

    @Override
    public void remove() {
        // TODO: complete this method
    }

    @Override
    public boolean exists() {
        // TODO: complete this method
        return false;
    }
}
