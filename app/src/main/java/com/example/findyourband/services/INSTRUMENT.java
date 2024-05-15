package com.example.findyourband.services;

import com.example.findyourband.R;

public enum INSTRUMENT {
    Электрогитара(R.drawable.ic_guitar),
    Бас(R.drawable.ic_bass),
    Ударные(R.drawable.ic_drum),
    Клавишные(R.drawable.ic_piano),
    Вокал(R.drawable.ic_vocal),
    Саксофон(R.drawable.ic_sax),
    Труба(R.drawable.ic_trumpet),
    Виолончель(R.drawable.ic_cello),
    Скрипка(R.drawable.ic_violin),
    Контрабас(R.drawable.ic_contrabass),
    Мультиинструменталист(R.drawable.ic_multiinstrumental),
    Другое(R.drawable.ic_other);

    private final int imageId;

    INSTRUMENT(int i) {
        imageId = i;
    }

    public int getImageId() {
        return imageId;
    }
}
