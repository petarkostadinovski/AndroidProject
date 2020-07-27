package com.example.firebaseproject.Service.Implementation;

import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.Keychain;
import com.example.firebaseproject.Service.AccessoriesService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AccessoriesServiceImplementation implements AccessoriesService {
    @Override
    public List<Keychain> filterKeyByNameAsc(List<Keychain> keychainList) {
        return keychainList.stream().sorted(Comparator.comparing(Keychain::getName)).collect(Collectors.toList());
    }

    @Override
    public List<Keychain> filterKeyByNameDesc(List<Keychain> keychainList) {
        return keychainList.stream()
                .sorted(Comparator.comparing(Keychain::getName).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Keychain> filterAvailableKeys(List<Keychain> keychainList) {
        return keychainList.stream()
                .filter(keychain -> keychain.getOn_stock() == 1)
                .collect(Collectors.toList());
    }

    @Override
    public List<Keychain> filterNotAvailableKeys(List<Keychain> keychainList) {
        return keychainList.stream()
                .filter(keychain -> keychain.getOn_stock() == 0)
                .collect(Collectors.toList());
    }
}
