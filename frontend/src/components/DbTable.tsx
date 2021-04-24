import {DbPost} from "../model/DbPosts";
import React from "react";

export interface IProps {
    dbPosts: DbPost[]
}

export interface IState {
}

export class DbTableComponent extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <table className={"Post-table"}>
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Ip</th>
                        <th>Message</th>
                        <th>Session ID</th>
                        <th>Submission date</th>
                    </tr>
                </thead>
                <tbody>
                {
                    this.props.dbPosts.map((post: DbPost, i: number) => (
                        <tr key={i}>
                            <td>{post.tutorialId}</td>
                            <td>{post.ip}</td>
                            <td>{post.message}</td>
                            <td>{post.sessionId}</td>
                            <td>{post.submissionDate}</td>
                        </tr>
                    ))
                }
                </tbody>
            </table>
        )
    }
}