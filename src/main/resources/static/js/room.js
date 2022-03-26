class Room {
  state = null; // AppState
  buffur = [];

  constructor(stompClient, roomId) {
    this.stompClient = stompClient;
    this.roomId = roomId;
  }

  subscribe() {
    this.stompClient.subscribe(`/app/room/${roomId}`, (message) => {
      console.log(message);
    });
    this.stompClient.subscribe(`/topic/room/${roomId}`, (message) => {
      console.log(message);
    });
  }

  init() {
    // Receive initial state
    const appSubscription = this.stompClient.subscribe(`/app/room/${roomId}`, (message) => {
      this.state = JSON.parse(message.body);
      appSubscription.unsubscribe();

      // Consume buffered messages
      this.buffur.forEach(message => this.handleMessage(message));
      this.buffur = [];
    });

    // Receive events
    this.stompClient.subscribe(`/topic/room/${roomId}`, (message) => {
      if (this.state !== null) {
        this.handleMessage(JSON.parse(message.body));
      } else {
        this.buffur.push(JSON.parse(message.body));
      }
    });
  }

  handleMessage(message) {
    check(this.state !== null, 'Illegal state: state is not initialized');

    console.log({ state: this.state, message });
  }

  join() {
    this.stompClient.send(`/app/room/${roomId}/join`);
  }

  close() {
    this.stompClient.send(`/app/room/${roomId}/leave`);
    this.stompClient.disconnect();
  }
}


function check(condition, message) {
  if (!condition) {
    throw new Error(message);
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
