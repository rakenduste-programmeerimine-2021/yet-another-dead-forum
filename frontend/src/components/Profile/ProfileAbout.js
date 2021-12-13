import {Button, Empty} from 'antd';
import { Card } from 'antd';
import {Context} from "../../store";
import {useContext} from "react";


const ProfileAbout = ({about, username}) => {

    const [state, dispatch] = useContext(Context);

    return (
        <>
        <Card title="About me" style={{marginBottom:'50px'}}>
            {about
                ?
                    <p>{about}</p>
                :
                <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={"This user has nothing to say about themselves"}/>
            }
            {
                (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.username === username)
                &&
                <Button style={{float:'right'}} href={`/user/${username}/edit/about`}>Edit About</Button>
            }
        </Card>

        </>


    )
}

export default ProfileAbout