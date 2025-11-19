package com.example.server.logic.data;

import com.example.server.logic.User;
import java.util.ArrayList;
import java.util.UUID;

public class DataRepository {

  // region [Singleton]

  public static DataRepository getInstance(){
    return instance;
  }
  public static synchronized void createInstance(){
    instance = new DataRepository();
  }
  private static DataRepository instance;
  private DataRepository(){}

  // endregion

  // region [User related methods]

  public void addUserToDB(
    UserData _userData
  ) {

    userDB.saveUser(_userData);

  }
  public void addUser(
    User _user
  ) {
    users.add(_user);
  }
  public void removeUser(
    User _user
  ) {
    users.remove(_user);
  }
  public boolean userLoggedIn(
    String _username
  ) {
    for(User user: users){
      if(user.userData.username.equals(_username)){
        return true;
      }
    }
    return false;
  }
  public UserData findUserByUsername(
    String _username
  ) {

    ArrayList<UserData> userDataList = userDB.loadUsers();

    // Find the user by username
    UserData userData = null;
    for(UserData userData_: userDataList){
      if(userData_.username.equals(_username)){
        userData = userData_;
        break;
      }
    }

    return userData;

  }
  public UserData getUserByID(
      UUID _userID
  ) {

    ArrayList<UserData> users = getUsersByIDList( new UUID[]{_userID} );

    if(users.isEmpty()){
      return null;
    }

    return users.getFirst();

  }
  public ArrayList<UserData> getUsersByIDList(
      UUID[] _userIDArray
  ) {

    ArrayList<UserData> fullUserDataList = userDB.loadUsers();

    ArrayList<UserData> returnUserDataList = new ArrayList<>();

    for(UserData userData: fullUserDataList){

      boolean userAlreadyFound = false;
      for(UserData returnUserData: returnUserDataList){
        if(returnUserData.ID.equals(userData.ID)){
          userAlreadyFound = true;
          break;
        }
      }
      if(!userAlreadyFound){
        for(UUID searchID: _userIDArray){
          if(searchID.equals(userData.ID)){
            returnUserDataList.add(userData);
            break;
          }
        }
      }
    }

    return returnUserDataList;

  }

  // endregion

  // region [Global room related methods]
  public void addRoomToDB(
    RoomData _roomData
  ) {

    roomDB.saveRoom(_roomData);

  }
  public ArrayList<RoomData> fetchRoomsFromDB(){

    return roomDB.loadRooms();
  }

  public ArrayList<MessageDataNetwork> fetchRoomMessagesByRoomID(
    UUID _roomID
  ) {

    ArrayList<MessageData> fullMessageDataList = messageDB.loadMessages();

    // Filter messages only for the requested roomID
    ArrayList<MessageData> messageDataList = new ArrayList<>(
        fullMessageDataList.stream().filter( _messageData -> { return _messageData.roomID.equals(_roomID); } ).toList()
    );

    // Need to find all unique user ID's to later prepare messages for sending
    ArrayList<UUID> uniqueUserIDList = new ArrayList<>();
    for(MessageData messageData: messageDataList) {

      boolean idInList = false;
      for(UUID userID: uniqueUserIDList){
        if(userID.equals(messageData.userID)){
          idInList = true;
          break;
        }
      }
      if(!idInList){
        uniqueUserIDList.add(messageData.userID);
      }

    }

    ArrayList<UserData> users = getUsersByIDList(uniqueUserIDList.toArray( new UUID[0] ));

    // Create MessageDataNetwork array and use users as a helper to identify usernames
    ArrayList<MessageDataNetwork> returnMessageDataList = new ArrayList<>();

    for(MessageData messageData: messageDataList){

      String username = null;
      for(UserData user: users){
        if(messageData.userID.equals(user.ID)){
          username = user.username;
        }
      }

      returnMessageDataList.add( new MessageDataNetwork(
          username,
          messageData.message
      ));

    }

    return returnMessageDataList;

  }

  // endregion

  // region [Private room related methods]

  public void addPrivateRoomToDB(
      PrivateRoomData _roomData
  ) {

    privateRoomDB.saveRoom(_roomData);

  }
  public ArrayList<PrivateRoomData> fetchPrivateRoomsForUser(
      UUID _userID
  ) {

    ArrayList<PrivateRoomData> dbPrivateRoomDataList = privateRoomDB.loadRooms();

    ArrayList<PrivateRoomData> returnPrivateRoomDataList = new ArrayList<>();

    for(PrivateRoomData roomData: dbPrivateRoomDataList) {
      if(_userID.equals(roomData.user1.ID) || _userID.equals(roomData.user2.ID)){
        returnPrivateRoomDataList.add(roomData);
      }
    }
    return returnPrivateRoomDataList;

  }
  public boolean checkPrivateRoomExists(
      UUID user1ID,
      UUID user2ID
  ) {

    boolean exists = false;

    ArrayList<PrivateRoomData> roomDataList = privateRoomDB.loadRooms();
    for(PrivateRoomData roomData: roomDataList){

      if(
          (roomData.user1.ID.equals(user1ID) || roomData.user1.ID.equals(user2ID))
              &&
              (roomData.user2.ID.equals(user1ID) || roomData.user2.ID.equals(user2ID))
      ) {
        exists = true;
        break;
      }

    }

    return exists;

  }

  // endregion

  public void addMessageToDB(
    MessageData _messageData
  ) {

    messageDB.saveMessage(_messageData);

  }

  private final ArrayList<User> users = new ArrayList<>();
  private MessageDB messageDB = new MessageDB();
  private RoomDB roomDB = new RoomDB();
  private PrivateRoomDB privateRoomDB = new PrivateRoomDB();
  private UserDB userDB = new UserDB();

}
