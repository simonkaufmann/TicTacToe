import React from 'react';
import './App.css';

import { useState, useEffect } from 'react';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import FormControlLabel from '@material-ui/core/FormControlLabel';

import Skeleton from './Skeleton.js';
import WinLoseSnackbar from './WinLoseSnackbar.js';

var ReactFitText = require('react-fittext');

const myStyles = (theme) => ({
  square: {
    background: "#fff",
    border: "1px solid #444",
    //fontSize: "20vw",
    fontWeight: "bold",
    textAlign: "center",
    height: "100%",
    width: "100%",
    "&:focus": {
      outline: "none",
    },
  },
  squareBox: {
    width: "33%",
    height: "33%",
    padding: "0",
  },
  boardRow: {
    padding: "0",
    width: "100%",
  },
  board: {
    borderCollapse: "collapse",
    borderSpacing: "0",
    width: "100%",
    height: "100%",
  },
});

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1),
  },
  toolbar: theme.mixins.toolbar,
  myContainer: {
    textAlign: "center",
    paddingTop: theme.spacing(3),
    [theme.breakpoints.up('sm')]: {
      paddingLeft: (theme.spacing(1) + 240) + "px",
    },
  },
  boardBox: {
    display: "inline-block",
    width: "85%",
    [theme.breakpoints.up('md')]: {
      width: "60%",
    },
    maxWidth: "450px",
  },
  paper: {
    padding: theme.spacing(3),
  },
}));

class Square_ extends React.Component {

  constructor(props) {
    super(props);
    const { classes } = props;    
    this.classes = classes;
  }

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
    if (this.valueToChar() === ' ')
    {
      let json = { field: this.props.fieldNumber };
      fetch('/api/game/' + this.props.id + '/send-move/', {
        method: 'post',
        body: JSON.stringify(json)
      });
      this.props.callBack(this.props.fieldNumber);
    }
  }

  render() {
    return (
      <td className={this.classes.squareBox}>
        <button
          className={this.classes.square}
          onClick={this.clickSquare}
        >
          <ReactFitText compressor={0.25}>
          <h1>{this.valueToChar()}</h1>
          </ReactFitText>
        </button>
      </td>
    );
  }
}

class Board_ extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      result: ' ',
      width: null,
    };
    const { classes } = props;
    this.props = props;
    this.classes = classes;
  }

  componentDidMount = () => {
    this.timer = setInterval(this.updateBoard, 1000);

    this.setState({
      width: this.container.offsetWidth,
    });

    window.addEventListener("resize", this.updateDimensions);
  }

  updateDimensions = () => {
    this.setState({
      width: this.container.offsetWidth,
    });
  }

  renderSquare = (i) => {
    return <Square fieldNumber={i} value={this.props.board[i]}
                   callBack={this.squareClicked} id={this.props.id}/>;
  }

  squareClicked = (i) => {
    let b = this.props.board;
    if (b[i] === 0) {
      console.log(this.props.player);
      if (this.props.player === 'X')
      {
        b[i] = 1;
      } else {
        b[i] = 2;
      }
      this.props.setBoard(b);
    }
  }
  
  updateBoard = () => {
    fetch('/api/game/' + this.props.id + '/get-move')
      .then((response) => response.json())
      .then(json => {
        for (let i = 0; i < this.props.board.length; i++)
        {
          if (this.props.board[i] !== 0 && json.state[i] === 0)
          {
            // if move returned by server does not contain latest
            // move of player (because of network delays and unfortunate
            // timing), then do not accept move
            return;
          }
        }
        this.props.setBoard(json.state);
        this.setState({result: json.result });
      }).catch(() => {
        console.log("Error update board");
      });
  }
  
  renderContent() {
    const { width } = this.state;
    return (
      <div style={{ width: "100%", height: width }}>
        <table className={this.classes.board}>
          <tbody>
            <tr className={this.classes.boardRow}>
              {this.renderSquare(0)}
              {this.renderSquare(1)}
              {this.renderSquare(2)}
            </tr>
            <tr className={this.classes.boardRow}>
              {this.renderSquare(3)}
              {this.renderSquare(4)}
              {this.renderSquare(5)}
            </tr>
            <tr className={this.classes.boardRow}>
              {this.renderSquare(6)}
              {this.renderSquare(7)}
              {this.renderSquare(8)}
            </tr>
          </tbody>
        </table>
        <WinLoseSnackbar result={this.state.result} />
      </div>
    );
  }

  render() {
    const { width } = this.state;
    return (
      <div ref={el => (this.container = el)}>
        { this.renderContent() }
      </div>
    );
  }
}

const Board = withStyles(myStyles)(Board_);
const Square = withStyles(myStyles)(Square_);

export default function Home() {

  const [state, setState] = useState({
    id: "",
    player: "X",
    board: [0, 0, 0, 0, 0, 0, 0, 0, 0],
  });

  useEffect(() => {
    if (state.id === "") {
      startGame(state.player);
    }
  });

  function startGame(player) {
    let url = "";
    if (player === 'O')
    {
      url = '/api/game/start-game-player-o';
    } else {
      url = '/api/game/start-game-player-x';
    }
    fetch(url)
      .then((response) => response.json())
      .then(json => {
        setState(state => ({...state, id: json.id }));
      }).catch(() => {
        console.log("Error start game");
      });
      setBoard([0, 0, 0, 0, 0, 0, 0, 0, 0]);
  }

  function changePlayer(event)
  {
    console.log(event.target.value);
    setState(state => ({...state, player: event.target.value}));
    startGame(event.target.value);
  }

  function setBoard(board)
  {
    setState(state => ({...state, board:board}));
  }

  const classes = useStyles();

  return (
    <div>
      <Skeleton /> 
      <div className={classes.toolbar}/>
      <Container className={classes.myContainer}>
        <div className={classes.boardBox}>
          <Board id={state.id} player={state.player} board={state.board} setBoard={setBoard}/>
          <Container>
            <FormControlLabel control={<Radio checked={state.player === 'X'} onChange={changePlayer} value={'X'}/>} label="Player X" />
            <FormControlLabel control={<Radio checked={state.player === 'O'} onChange={changePlayer} value={'O'}/>} label="Player X" />
          </Container>
          <Button variant="contained" color="primary" className={classes.button} onClick={() => startGame(state.player)}>
            Reset Game
          </Button>
          <Button variant="contained" color="secondary" className={classes.button} onClick={() => startGame(state.player)}>
            Start Game
          </Button>
        </div>
      </Container>
    </div>
  );
}


