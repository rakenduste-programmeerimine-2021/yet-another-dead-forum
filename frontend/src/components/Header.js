import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { Menu } from 'antd';
import { Context } from '../store';
import { logoutUser, updatePage } from '../store/actions';

const Header = () => {
  const [state, dispatch] = useContext(Context);

  const handleLogout = () => {
    dispatch(logoutUser());
    dispatch(updatePage('forum'));
  }

  return (
    <Menu theme="dark" mode="horizontal" selectedKeys={[state.page.current]}>
      <Menu.Item key={'sitename'} onClick={e => dispatch(updatePage(e.key))}>
        <Link style={{fontWeight:'bold'}} to="/">Yet Another Dead Forum</Link>
      </Menu.Item>
      <Menu.Item key={'forum'} onClick={e => dispatch(updatePage(e.key))}>
        <Link to="/">Forums</Link>
      </Menu.Item>
      {state.auth.token &&
        (
          <>
            <Menu.Item key={'profile'} onClick={e => dispatch(updatePage(e.key))}>
              <Link to={'/user/' + state.auth.user.username}>Profile</Link>
            </Menu.Item>
              {state.auth.user.roles.includes('ROLE_ADMIN') &&
                  <Menu.Item key={'configure'} onClick={e => dispatch(updatePage(e.key))}>
                      <Link to="/configure">Configure</Link>
                  </Menu.Item>
              }

            <Menu.Item key={'logout'} onClick={handleLogout}>
              <Link to="#">Logout</Link>
            </Menu.Item>
          </>
        )
      }
      {!state.auth.token &&
        (
          <>
            <Menu.Item key={'login'} onClick={e => dispatch(updatePage(e.key))}>
              <Link to="/login">Login</Link>
            </Menu.Item>
            <Menu.Item key={'register'} onClick={e => dispatch(updatePage(e.key))}>
              <Link to="/register">Register</Link>
            </Menu.Item>
          </>
        )
      }
    </Menu>
  );
};

export default Header;