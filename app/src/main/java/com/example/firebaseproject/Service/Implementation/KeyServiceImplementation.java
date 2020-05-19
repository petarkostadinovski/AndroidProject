package com.example.firebaseproject.Service.Implementation;

import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Service.KeyService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KeyServiceImplementation implements KeyService {
    @Override
    public List<Key> filterKeyByNameAsc(List<Key> keyList) {
        return keyList.stream().sorted(Comparator.comparing(Key::getName)).collect(Collectors.toList());
    }

    @Override
    public List<Key> filterKeyByNameDesc(List<Key> keyList) {
        return keyList.stream()
                .sorted(Comparator.comparing(Key::getName).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Key> filterAvailableKeys(List<Key> keyList) {
        return keyList.stream()
                .filter(key -> key.getOn_stock() == 1)
                .collect(Collectors.toList());
    }

    @Override
    public List<Key> filterNotAvailableKeys(List<Key> keyList) {
        return keyList.stream()
                .filter(key -> key.getOn_stock() == 0)
                .collect(Collectors.toList());
    }
}
