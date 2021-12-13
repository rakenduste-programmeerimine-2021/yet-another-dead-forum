import {Spin, Typography} from 'antd';
import {useContext, useEffect, useState} from "react";
import {Context} from "../store";
import {useParams} from "react-router-dom";

const Profile = () => {

    const [profile, setProfile] = useState()
    const [state, dispatch] = useContext(Context)
    const { Title } = Typography
    const params = useParams()

    useEffect(async () => {
        setProfile(await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/user/${params.username}/profile`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + state.auth.token
            }
        }).then(r => r.json()))
    }, [])

  return(
    <>
        {
            profile
                ?
            <>
            <Title style={{textAlign: 'center'}}>{profile.displayName}'s profile</Title>
            <div>Test</div>
            </>
                :
            <Spin />
        }
    </>
  )
};

export default Profile;