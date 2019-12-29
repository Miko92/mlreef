import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import mlReefIcon01 from '../../images/MLReef_Logo_navbar.png';
import arrowDownWhite01 from '../../images/arrow_down_white_01.svg';
import arrowDownBlue01 from '../../images/arrow_down_blue_01.svg';
import './navbar.css';

class Navbar extends Component {
  constructor(props) {
    super(props);
    this.state = { dialogOpen: false, projectDialog: false, yourProjects: false };
    this.handleProject = this.handleProject.bind(this);
    this.handleOutsideClick = this.handleOutsideClick.bind(this);
  }

  componentWillUnmount() {
    this.setState = (state) => (state);
  }

  handleProfile = () => {
    const { dialogOpen } = this.state;
    if (!dialogOpen) {
      document.addEventListener('click', this.handleOutsideClick, false);
    } else {
      document.removeEventListener('click', this.handleOutsideClick, false);
    }

    this.setState((prevState) => ({
      dialogOpen: !prevState.dialogOpen,
    }));
  }

  handleOutsideClick = () => {
    const { dialogOpen, projectDialog } = this.state;
    if (dialogOpen) this.handleProfile();
    else if (projectDialog) this.handleProject();
  }

  handleProject(e) {
    const { projectDialog } = this.state;
    if (!e) {
      return;
    }
    switch (e.target.id) {
      case 'your-projects':
        this.setState({ yourProjects: true });
        break;

      default:
        if (!projectDialog) {
          document.addEventListener('click', this.handleOutsideClick, false);
        } else {
          document.removeEventListener('click', this.handleOutsideClick, false);
        }
        this.setState((prevState) => ({
          projectDialog: !prevState.projectDialog,
        }));
        break;
    }
  }

  render() {
    const { dialogOpen, projectDialog, yourProjects } = this.state;
    return (
      <div className="navbar">
        <Link to="/">
          <img className="logo" src={mlReefIcon01} alt="" />
        </Link>
        <div
          role="button"
          tabIndex="0"
          className={
            projectDialog
              ? 'projects-dropdown-click'
              : 'projects-dropdown'
          }
          onClick={this.handleProject}
          onKeyDown={this.handleProject}
        >
          <a href="#foo">Projects</a>
          <img
            className="dropdown-white"
            src={
              projectDialog
                ? arrowDownBlue01
                : arrowDownWhite01
            }
            alt=""
          />
          {projectDialog && (
            <div className="project-box">
              <div className="user-projects">
                <p><Link to="/my-projects">Your Projects</Link></p>
                <p>Starred Projects</p>
                <p>Explore Projects</p>
              </div>
              {!yourProjects && (
              <div className="project-search">
                <input
                  type="text"
                  placeholder="Search your projects"
                />
                <div style={{ margin: '1em' }}>
                  <b>Frequently visited</b>
                </div>
              </div>
              )}
            </div>
          )}
        </div>

        <div
          role="button"
          tabIndex="0"
          className={
            `profile-options ${
              dialogOpen ? 'selected-controller' : ''}`
          }
          onClick={this.handleProfile}
          onKeyDown={this.handleProfile}
          ref={(node) => {
            this.node = node;
          }}
        >
          <img
            className="dropdown-white"
            src={
              dialogOpen ? arrowDownBlue01 : arrowDownWhite01
            }
            alt=""
          />
          <div
            className={
              dialogOpen
                ? 'profile-pic-darkcircle'
                : 'profile-pic-circle'
            }
          />
          {dialogOpen && (
            <div className="sign-box">
              <div>
                Signed in as
                {' '}
                <b>camillo</b>
              </div>
              <hr />
              <p>Set Status</p>
              <p>Your Profile</p>
              <p>Settings</p>
              <hr />
              <p>Sign Out</p>
            </div>
          )}
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    projectsList: state.projects,
  };
}

export default connect(mapStateToProps)(Navbar);