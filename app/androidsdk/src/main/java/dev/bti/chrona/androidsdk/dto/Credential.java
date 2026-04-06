package dev.bti.chrona.androidsdk.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import dev.bti.chrona.androidsdk.constants.CredentialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Credential implements Parcelable {
    private String credential;
    private CredentialType credentialType;

    protected Credential(Parcel in) {
        credential = in.readString();
        String credentialTypeName = in.readString();
        credentialType = credentialTypeName != null ? CredentialType.valueOf(credentialTypeName) : null;
    }

    public static final Creator<Credential> CREATOR = new Creator<>() {
        @Override
        public Credential createFromParcel(Parcel in) {
            return new Credential(in);
        }

        @Override
        public Credential[] newArray(int size) {
            return new Credential[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(credential);
        dest.writeString(credentialType != null ? credentialType.name() : null);
    }

    public String mask() {
        if (credentialType != CredentialType.EMAIL) {
            return "";
        }

        String[] strings = credential.split("@");

        StringBuilder builder = new StringBuilder();
        builder.append(strings[0].charAt(0));

        for (int i = 0; i < strings[0].length() - 2; i++) {
            builder.append("*");
        }

        builder.append(strings[1]);
        return builder.toString();
    }

    public static Credential of(String credential) {
        CredentialType type;

        if (credential.contains("@")) {
            type = CredentialType.EMAIL;
        } else if (credential.matches("[a-zA-Z0-9]{16}") ||
                credential.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            type = CredentialType.UID;
        } else if (credential.matches("^\\+?[0-9\\-\\s()]{7,15}$")) {
            type = CredentialType.PHONE;
        } else {
            type = CredentialType.USERNAME;
        }

        return Credential.builder()
                .credential(credential)
                .credentialType(type)
                .build();
    }
}
