export const USER_CREATE = 'USER_CREATE';

export const createUser = data => ({
  type: USER_CREATE,
  payload: data,
});
