import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Layout } from 'antd';
import 'antd/dist/antd.css';

import { default as PageHeader } from './components/Header';
import Forum from './pages/Forum';
import Profile from './pages/Profile';
import Configure from './pages/Configure';
import Login from './pages/Login';
import Register from './pages/Register';

function App() {
  const { Header, Content, Footer } = Layout;
  return (
    <BrowserRouter>
      <Layout className="layout" style={{height: '100%'}}>
        <Header>
          <PageHeader />
        </Header>
        <Content style={{padding: '50px'}}>
          <div style={{background: '#fff', height: '100%', padding: '20px'}}>
            <Routes>
              <Route exact path="/" element={<Forum />} />
              <Route exact path="/profile" element={<Profile />} />
              <Route exact path="/configure" element={<Configure />} />
              <Route exact path="/login" element={<Login />} />
              <Route exact path="/register" element={<Register />} />
            </Routes>
          </div>
        </Content>
        <Footer theme="dark" style={{textAlign: 'center'}}>&copy; { (new Date().getFullYear()) } Yet Another Dead Forum</Footer>
      </Layout>
    </BrowserRouter>
  );
}

export default App;
