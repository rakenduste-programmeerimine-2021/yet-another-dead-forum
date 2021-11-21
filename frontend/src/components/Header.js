import { useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import { Menu } from 'antd';
import { Context } from '../store';
import { logoutUser } from '../store/actions';

const Header = () => {
  const [current, setCurrent] = useState(window.location.pathname.replace('/', ''));
  const [state, dispatch] = useContext(Context);

  const handleLogout = () => {
    dispatch(logoutUser());
  }

  return (
    <Menu theme="dark" mode="horizontal" selectedKeys={[current]}>
      <Menu.Item key={'sitename'}>Yet Another Dead Forum</Menu.Item>
      <Menu.Item key={''} onClick={e => setCurrent(e.key)}>
        <Link to="/">Forums</Link>
      </Menu.Item>
      {state.auth.token &&
        (
          <Menu.Item key={'logout'} onClick={handleLogout}>
            <Link to="#">Logout</Link>
          </Menu.Item>
        )
      }
      {!state.auth.token &&
        (
          <>
            <Menu.Item key={'login'} onClick={e => setCurrent(e.key)}>
              <Link to="/login">Login</Link>
            </Menu.Item>
            <Menu.Item key={'register'} onClick={e => setCurrent(e.key)}>
              <Link to="/register">Register</Link>
            </Menu.Item>
          </>
        )
      }
    </Menu>
  );
};

export default Header;