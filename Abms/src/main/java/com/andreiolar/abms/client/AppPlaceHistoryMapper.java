package com.andreiolar.abms.client;

import com.andreiolar.abms.client.place.AdminPlace;
import com.andreiolar.abms.client.place.LoginPlace;
import com.andreiolar.abms.client.place.PasswordRecoveryPlace;
import com.andreiolar.abms.client.place.UserPlace;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({LoginPlace.Tokenizer.class, UserPlace.Tokenizer.class, AdminPlace.Tokenizer.class, PasswordRecoveryPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
