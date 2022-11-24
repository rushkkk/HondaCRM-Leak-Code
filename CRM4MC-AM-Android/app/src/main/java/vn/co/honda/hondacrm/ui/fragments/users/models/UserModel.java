/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.co.honda.hondacrm.ui.fragments.users.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * User Entity used in the data layer.
 */
public class UserModel implements Parcelable {

  @SerializedName("id")
  private int userId;

  @SerializedName("cover_url")
  private String coverUrl;

  @SerializedName("full_name")
  private String fullname;

  @SerializedName("description")
  private String description;

  @SerializedName("followers")
  private int followers;

  @SerializedName("email")
  private String email;

  public UserModel() {
    //empty
  }

  protected UserModel(Parcel in) {
    userId = in.readInt();
    coverUrl = in.readString();
    fullname = in.readString();
    description = in.readString();
    followers = in.readInt();
    email = in.readString();
  }

  public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
    @Override
    public UserModel createFromParcel(Parcel in) {
      return new UserModel(in);
    }

    @Override
    public UserModel[] newArray(int size) {
      return new UserModel[size];
    }
  };

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getCoverUrl() {
    return coverUrl;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getDescription() {
    return description;
  }

  public int getFollowers() {
    return followers;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(userId);
    dest.writeString(coverUrl);
    dest.writeString(fullname);
    dest.writeString(description);
    dest.writeInt(followers);
    dest.writeString(email);
  }
}
