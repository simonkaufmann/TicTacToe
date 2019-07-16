import React from 'react';
import './App.css';

import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Button from '@material-ui/core/Button';

import ButtonAppBar from './ButtonAppBar.js';
import Drawer from './Drawer.js';

var ReactFitText = require('react-fittext');

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1),
  },
  toolbar: theme.mixins.toolbar
}));

class Square extends React.Component {

  valueToChar = () => {
    if (this.props.value === 1) {
      return 'X';
    } else if (this.props.value === 2){
      return 'O';
    } else {
      return ' ';
    }
  }

  clickSquare = () => {
    // send move
    let json = { field: this.props.fieldNumber };
    fetch('/api/send-move/', {
      method: 'post',
      body: JSON.stringify(json)
    });
    this.props.callBack(this.props.fieldNumber);
  }

  render() {
    return (
      <div className = "square-box">
        <button
          className="square"
          onClick={this.clickSquare}
        >
          <ReactFitText compressor={0.25}>
          <h1>{this.valueToChar()}</h1>
          </ReactFitText>
        </button>
      </div>
    );
  }
}

class Board extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      board: [0, 0, 0, 0, 0, 0, 0, 0, 0],
      result: ' '
    };
  }

  componentDidMount = () => {
    this.timer = setInterval(this.updateBoard, 1000);
  }

  renderSquare = (i) => {
    return <Square fieldNumber={i} value={this.state.board[i]}
                   callBack={this.squareClicked}/>;
  }

  squareClicked = (i) => {
    let b = this.state.board;
    if (b[i] === 0) {
      b[i] = 1;
      this.setState({board: b});
    }
  }
  
  updateBoard = () => {
    // update board
    fetch('/api/get-move')
      .then((response) => response.json())
      .then(json => {
        this.setState({ board: json.state, result: json.result });
      }).catch(() => {
        console.log("Error update board");
      });
  }

  render() {
    return (
      <div className="board-outer-box">
        <div className="board-box">
          <table className="board">
            <tbody>
              <tr className="board-row"><td className="board-row">
                {this.renderSquare(0)}
                {this.renderSquare(1)}
                {this.renderSquare(2)}
              </td></tr>
              <tr className="board-row"><td className="board-row">
                {this.renderSquare(3)}
                {this.renderSquare(4)}
                {this.renderSquare(5)}
              </td></tr>
              <tr className="board-row"><td className="board-row">
                {this.renderSquare(6)}
                {this.renderSquare(7)}
                {this.renderSquare(8)}
              </td></tr>
            </tbody>
          </table>
          {this.state.board}
          {this.state.result}
        </div>
      </div>
    );
  }
}

export default function Home() {
  const classes = useStyles();

  const [state, setState] = React.useState({
    drawerOpen: false,
  });

  function startGame() {
    fetch("api/start-game");  
  }   

  function toggleDrawer(argOpen) {
    setState({...state, drawerOpen: argOpen});
  }

  return (
    <div>
      <ButtonAppBar open={state.drawerOpen} drawerOpen={state.drawerOpen} toggleDrawer={toggleDrawer}/>
      <Drawer open={state.drawerOpen} toggleDrawer={toggleDrawer}/>
      <Container style={{textAlign: "center"}}>
        <div className={classes.toolbar}/>
        <p> It's your turn </p>
        <Board/>
        <Button variant="contained" color="primary" className={classes.button}>
          Reset Game
        </Button>
        <Button variant="contained" color="secondary" className={classes.button} onClick={startGame}>
          Start Game
        </Button>
      </Container>
    </div>
  );
}


