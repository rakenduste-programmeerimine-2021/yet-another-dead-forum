import { USER_REGISTER } from './actions';

const authReducer = (state, action) => {
  switch(action.type) {
    case USER_REGISTER:
      return {
        ...state,
        token: action.payload.token,
        user: action.payload.user,
      }
  }
};

export { authReducer };