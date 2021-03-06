import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Layout } from 'antd';
import 'antd/dist/antd.css';
import './App.css';

import { default as PageHeader } from './components/Header';
import Forum from './pages/Forum';
import Profile from './pages/Profile';
import Configure from './pages/Configure';
import Login from './pages/Login';
import Register from './pages/Register';
import Posts from './pages/Posts';
import ThreadAdd from './components/Thread/ThreadAdd';
import ThreadEdit from './components/Thread/ThreadEdit';
import PostEdit from './components/Post/PostEdit';
import ProfileAboutEdit from "./components/Profile/ProfileAboutEdit";
import ProfileSignatureEdit from "./components/Profile/ProfileSignatureEdit";
import ConfigureUserRankTabs from "./components/Configure/ConfigureUserRankTabs";

function App() {
  const { Header, Content, Footer } = Layout;
  return (
    <BrowserRouter>
      <Layout className="forum-layout">
        <Header className="forum-header">
          <PageHeader />
        </Header>
        <Content className="forum-content">
          <div className="main-content">
            <Routes>
              <Route exact path="/" element={<Forum />} />
              <Route exact path="/user/:username" element={<Profile />} />
              <Route exact path="/user/:username/edit/about" element={<ProfileAboutEdit />} />
              <Route exact path="/user/:username/edit/signature" element={<ProfileSignatureEdit />} />
              <Route exact path="/configure" element={<Configure />} />
              <Route exact path="/configure/user/:username/roles" element={<ConfigureUserRankTabs />} />
              <Route exact path="/login" element={<Login />} />
              <Route exact path="/register" element={<Register />} />
              <Route exact path="/thread/:id" element={<Posts />} />
              <Route exact path="/thread/add" element={<ThreadAdd />} />
              <Route exact path="/thread/edit/:id" element={<ThreadEdit />} />
              <Route exact path="/post/edit/:id" element={<PostEdit />} />
            </Routes>
          </div>
        </Content>
        <Footer theme="dark" className="forum-footer">&copy; { (new Date().getFullYear()) } Yet Another Dead Forum</Footer>
      </Layout>
    </BrowserRouter>
  );
}

export default App;
