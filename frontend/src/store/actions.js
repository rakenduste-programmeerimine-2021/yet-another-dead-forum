export const USER_CREATE = 'USER_CREATE';
export const USER_LOGIN = 'USER_LOGIN';
export const USER_LOGOUT = 'USER_LOGOUT';

export const createUser = data => ({
  type: USER_CREATE,
  payload: data,
});

export const loginUser = data => ({
  type: USER_LOGIN,
  payload: data
});

export const logoutUser = () => ({
  type: USER_LOGOUT
});
