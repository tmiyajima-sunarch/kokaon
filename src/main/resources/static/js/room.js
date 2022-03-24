class Room {
  constructor(stompClient, roomId) {
    this.stompClient = stompClient;
    this.roomId = roomId;
  }

  subscribe() {
    this.stompClient.subscribe(`/topic/room/${roomId}`, (message) => {
      console.log(message);
    });
  }

  join() {
    this.stompClient.send(`/app/room/${roomId}/join`);
  }

  close() {
    this.stompClient.send(`/app/room/${roomId}/leave`);
    this.stompClient.disconnect();
  }
}

function initRoom(stompUrl, roomId) {
  const socket = new SockJS(stompUrl);
  const stompClient = webstomp.over(socket);

  return new Promise((resolve) => {
    stompClient.connect({ roomId }, (frame) => {
      resolve(new Room(stompClient, roomId));
    });
  });
}