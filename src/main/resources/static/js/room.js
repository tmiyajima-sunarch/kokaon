// @ts-check
/**
 * @callback EventCallback
 * @param {any} event
 * @return {void}
 */

class EventEmitter {
  /**
   * @type {Record<string, Set<EventCallback>>}
   */
  callbacks = {};

  /**
   * @param {string} type 
   * @param {EventCallback} callback 
   * @return {void}
   */
  on(type, callback) {
    if (this.callbacks[type]) {
      this.callbacks[type].add(callback);
    } else {
      this.callbacks[type] = new Set([callback]);
    }
  }

  /**
   * @param {string} type 
   * @param {EventCallback} callback 
   * @return {void}
   */
  off(type, callback) {
    if (this.callbacks[type]) {
      this.callbacks[type].delete(callback);
    }
  }

  /**
   * @param {string} type 
   * @param {any} event 
   */
  emit(type, event) {
    if (this.callbacks[type]) {
      this.callbacks[type].forEach(callback => callback(event));
    }
  }
}

/**
 * @typedef AppState
 * @property {UserData | null} me
 * @property {RoomData | null} room
 */
/**
 * @typedef RoomData
 * @property {string} id
 * @property {string} name
 * @property {UserData} owner
 * @property {UserData[]} members
 * @property {AudioData[]} audios
 */
/**
 * @typedef UserData
 * @property {string} id
 * @property {string} nickname
 */
/**
 * @typedef AudioData
 * @property {string} id
 * @property {string} name
 * @property {string} url
 */

class Room {
  /**
   * @type {AppState}
   */
  state = {
    me: null,
    room: null,
  };
  /**
   * @type {any[]}
   */
  buffer = [];

  emitter = new EventEmitter();

  constructor(stompClient, roomId) {
    this.stompClient = stompClient;
    this.roomId = roomId;
  }

  init() {
    // Receive initial state
    const appSubscription = this.stompClient.subscribe(`/app/room/${this.roomId}`, (message) => {
      appSubscription.unsubscribe();
      this.state = { ...this.state, room: JSON.parse(message.body) };
      this.emitter.emit('change', this.state);

      // Consume buffered messages
      this.buffer.forEach(message => this.handleMessage(message));
      this.buffer = [];
    });

    // Receive events
    this.stompClient.subscribe(`/topic/room/${this.roomId}`, (message) => {
      if (this.state !== null) {
        this.handleMessage(JSON.parse(message.body));
      } else {
        this.buffer.push(JSON.parse(message.body));
      }
    });
  }

  handleMessage(message) {
    if (message['@type'] === 'AudioPlayedEvent') {
      this.emitter.emit('play', message.audioId);
      return;
    }

    const newState = reducer(this.state, message);

    if (this.state !== newState) {
      this.state = newState;
      this.emitter.emit('change', this.state);
    }
  }

  /**
   * @param {'change'} type 
   * @param {EventCallback} callback 
   */
  on(type, callback) {
    this.emitter.on(type, callback);
  }

  /**
   * @param {'change'} type 
   * @param {EventCallback} callback 
   */
  off(type, callback) {
    this.emitter.off(type, callback);
  }

  enter(nickname) {
    const me = { id: Math.random().toString(32).substring(2), nickname };
    this.stompClient.send(`/app/room/${this.roomId}/enter`, JSON.stringify(me));
    this.state = { ...this.state, me };
  }

  play(audioId) {
    this.stompClient.send(`/app/room/${this.roomId}/play/${audioId}`)
  }

  close() {
    this.stompClient.send(`/app/room/${this.roomId}/leave`);
    this.stompClient.disconnect();
  }
}

/**
 * @param {AppState} state 
 * @param {any} message 
 * @returns {AppState}
 */
function reducer(state, message) {
  if (!state.room || state.room.id !== message.roomId) {
    return state;
  }

  switch (message['@type']) {
    case 'MemberEnteredEvent': {
      const { memberId, memberNickname } = message;
      if (state.room.members.some(member => member.id === memberId)) {
        return state;
      }
      return { ...state,
        room: {
          ...state.room,
          members: [
            ...state.room.members,
            {
              id: memberId,
              nickname: memberNickname,
            }
          ]
        }
      };
    }

    case 'MemberLeftEvent': {
      const { memberId } = message;
      if (!state.room.members.some(member => member.id === memberId)) {
        return state;
      }
      return { ...state,
        room: {
          ...state.room,
          members: state.room.members.filter(member => member.id !== memberId),
        }
      }
    }

    case 'AudioAddedEvent': {
      const { audioId, audioName, audioUrl } = message;
      if (state.room.audios.some(audio => audio.id === audioId)) {
        return state;
      }
      return {
        ...state,
        room: {
          ...state.room,
          audios: [
            ...state.room.audios,
            {
              id: audioId,
              name: audioName,
              url: audioUrl,
            },
          ],
        }
      };
    }

    case 'AudioRemovedEvent': {
      // TODO
      return state;
    }

    case 'RoomClosedEvent': {
      // TODO
      return state;
    }

    default: {
      console.warn('Unknown event received: ', message);
      return state;
    }
  }
}

/**
 * @param {boolean} condition 
 * @param {string} message 
 */
function check(condition, message) {
  if (!condition) {
    throw new Error(message);
  }
}
