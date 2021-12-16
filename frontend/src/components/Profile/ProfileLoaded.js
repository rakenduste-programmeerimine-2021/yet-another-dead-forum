import {Typography} from "antd";
import ProfileStats from "./ProfileStats";
import ProfileAbout from "./ProfileAbout";
import ProfileSignature from "./ProfileSignature";
import ProfileUserContent from "./ProfileUserContent";



const ProfileLoaded = ({profile}) => {

    const { Title } = Typography
    let stats = {
        createdAt: profile.createdAt,
        postAmount: profile.postAmount,
        threadAmount: profile.threadAmount,
        roles: profile.roles,
        visits: profile.visits }

    return (
        <>
            <Title level={4} style={{textAlign: 'center'}}>{profile['displayName']}'s profile</Title>
            <ProfileStats stats = {stats} />
            <ProfileAbout username={profile.username} about = {profile.about} />
            <ProfileSignature username={profile.username} signature={profile.signature}/>
            <ProfileUserContent username={profile.username}
                                posts={profile.posts}
                                threads={profile.threads}
                                displayName={profile.displayName}/>
        </>
    )
}

export default ProfileLoaded