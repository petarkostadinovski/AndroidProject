package com.example.firebaseproject.Service;

import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.Keychain;

import java.util.List;

public interface AccessoriesService {
    List<Keychain> filterKeyByNameAsc (List<Keychain> keychainList);

    List<Keychain> filterKeyByNameDesc (List<Keychain> keychainList);

    List<Keychain> filterAvailableKeys (List<Keychain> keychainList);

    List<Keychain> filterNotAvailableKeys (List<Keychain> keychainList);
}
