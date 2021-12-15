import {Spin} from 'antd';
import {useContext, useEffect, useState} from "react";
import {Context} from "../store";
import {useParams} from "react-router-dom";
import ProfileLoaded from "../components/Profile/ProfileLoaded";

const Profile = () => {

    const [profile, setProfile] = useState()
    const [error, setError] = useState()
    const [state] = useContext(Context)
    const params = useParams()

    useEffect(async () => {
        try {
            setProfile(await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/user/${params.username}/profile`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + state.auth.token
                }})
            .then(res => {
                if (!res.ok) throw new Error('Could not load the profile.')
                else return res.json()})
            .catch(e => setError(e.message())))
        } catch (e) {
            setError('Could not load profile')
        }
    }, [])

  return(
    <>
        {(!profile && !error) && <Spin style={{display: 'flex', justifyContent:'center'}} />}
        { state.auth.token ?
            (profile && !error)
                ?
                <ProfileLoaded profile={profile} />
                :
                <p style={{display: 'flex', justifyContent:'center'}}>{error}</p>
            :
            <p style={{display: 'flex', justifyContent:'center'}}>You must be logged in to view profiles</p>
        }
    </>
  )
}

export default Profile;