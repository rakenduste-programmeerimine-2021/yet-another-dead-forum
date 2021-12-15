import {Input, Space, Table, Typography} from 'antd';
import {useContext, useState} from "react";
import {Context} from "../../store";
import {SearchOutlined} from "@ant-design/icons";
import {Navigate, useNavigate} from "react-router-dom";
import Text from "antd/es/typography/Text";



const ConfigureLoaded = (props) => {
    const [error, setError] = useState()
    const [state] = useContext(Context)
    const [users, setUsers] = useState(props.users)
    const navigate = useNavigate();

    function displayRoles(roles)  {
        return roles.map(r => <span
            key={r['id']}
            style={{textAlign:'center', backgroundColor: r['bodyCss'], color: r['textCss'], padding:'5px 15px', margin:'5px'}}
        >{r['displayName']}</span>)
    }

    const columns = [
        {
            title: 'User',
            dataIndex: 'displayName',
            key: 'user',
            render: text => <a>{text}</a>,
            filterDropdown: ({setSelectedKeys, selectedKeys, confirm}) => {
                return <Input
                    autoFocus
                    value={selectedKeys[0]}
                    onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value]: [])}
                    placeholder="Username filter"
                    onPressEnter={() => confirm()}
                    onBlur={() => confirm()}
                >{}</Input>
            },
            filterIcon: () => {
                return <SearchOutlined/>
            },
            onFilter: (value, record) => record.username.includes(value.toLowerCase()),
            width: '30%',
        },
        {
            title: 'Roles',
            key: 'roles',
            dataIndex: 'roles',
            render: roles => displayRoles(roles),
        },
        {
            title: 'Action',
            key: 'action',
            render: (record) => (
                <Space size="middle">
                    <a onClick={() => handleDelete(record.username)}>Delete</a>
                </Space>
            ),
        },
    ];

    async function loadUsers() {
        try {
            setUsers(await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/users`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + state.auth.token
                }})
                .then(res => {
                    if (!res.ok) throw new Error('Could load users.')
                    else return res.json()}))
        } catch (e) {
            setError('Could not load users')
        }
    }

    function onChange(pagination, filters, sorter, extra) {
        console.log('params', pagination, filters, sorter, extra);
    }

    async function handleDelete(username) {
        if (window.confirm("Are you sure you want to delete the user\n" + username + "?")) {
            const res = await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/user/delete/username/` + username, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + state.auth.token
                }
            })

            if(res.ok) {
                await loadUsers()
            } else {
                let errors = ''
                if (res.json().error) {
                    errors = res.json().error
                } else {
                    for (let i = 0; i < res.json().msg.length; i++) {
                        errors += res.json().msg[i].param[0].toUpperCase() + res.json().msg[i].param.slice(1) + ' ' + res.json().msg[i].msg + '\n'
                    }
                }
                setError(errors)
            }
        }
    }

    return(
        <>
            { error && <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{ error }</Text> }
            <Table columns={columns} dataSource={users} rowKey="id" onChange={onChange}/>
        </>
    )
};

export default ConfigureLoaded;