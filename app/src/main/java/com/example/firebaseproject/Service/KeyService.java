package com.example.firebaseproject.Service;

import com.example.firebaseproject.Model.Key;

import java.util.List;

public interface KeyService {

    List<Key> filterKeyByNameAsc (List<Key> keyList);

    List<Key> filterKeyByNameDesc (List<Key> keyList);

    List<Key> filterAvailableKeys (List<Key> keyList);

    List<Key> filterNotAvailableKeys (List<Key> keyList);

}
