export const USER_CREATE = 'USER_CREATE';
export const USER_LOGIN = 'USER_LOGIN';
export const USER_LOGOUT = 'USER_LOGOUT';
export const THREADS_UPDATE = 'THREADS_UPDATE';
export const POSTS_UPDATE = 'POSTS_UPDATE';

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

export const updateThreads = array => ({
  type: THREADS_UPDATE,
  payload: array
});

export const updatePosts = data => ({
  type: POSTS_UPDATE,
  payload: data
});
