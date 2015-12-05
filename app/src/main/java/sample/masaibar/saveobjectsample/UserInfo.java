package sample.masaibar.saveobjectsample;

import java.io.Serializable;

/**
 * Created by masaibar on 2015/12/05.
 */
public class UserInfo implements Serializable {

    private String mName;
    private int mAge;
    private Sex mSex;

    public enum Sex {
        MALE,
        FEMALE
    }

    public UserInfo(String name, int age, Sex sex) {
        mName = name;
        mAge = age;
        mSex = sex;
    }

    public String getName() {
        return mName;
    }

    public int getAge() {
        return mAge;
    }

    public Sex getSex() {
        return mSex;
    }
}
