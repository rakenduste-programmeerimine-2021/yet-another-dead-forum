export const USER_CREATE = 'USER_CREATE';
export const USER_LOGIN = 'USER_LOGIN';
export const USER_LOGOUT = 'USER_LOGOUT';
export const THREADS_UPDATE = 'THREADS_UPDATE';
export const THREAD_UPDATE = 'THREAD_UPDATE';
export const THREAD_ADD = 'THREAD_ADD';
export const THREAD_EDIT = 'THREAD_EDIT';
export const THREAD_DELETE = 'THREAD_DELETE';
export const THREAD_RESET = 'THREAD_RESET';
export const POSTS_UPDATE = 'POSTS_UPDATE';
export const POST_DELETE = 'POST_DELETE';
export const PAGE_UPDATE = 'PAGE_UPDATE';

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

export const updateSingleThread = array => ({
  type: THREAD_UPDATE,
  payload: array
});

export const addThread = data => ({
  type: THREAD_ADD,
  payload: data
});

export const editThread = data => ({
  type: THREAD_EDIT,
  payload: data
});

export const deleteThread = data => ({
  type: THREAD_DELETE,
  payload: data
});

export const resetSingleThread = () => ({
  type: THREAD_RESET
});

export const updatePosts = data => ({
  type: POSTS_UPDATE,
  payload: data
});

export const deletePost = data => ({
  type: POST_DELETE,
  payload: data
});

export const updatePage = data => ({
  type: PAGE_UPDATE,
  payload: data
});
