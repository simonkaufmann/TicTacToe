import React from 'react';
import './App.css';

import { useState, useEffect } from 'react';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';

import Skeleton from './Skeleton.js';
import WinLoseSnackbar from './WinLoseSnackbar.js';

var ReactFitText = require('react-fittext');

const myStyles = (theme) => ({
  square: {
    background: "#fff",
    border: "1px solid #444",
    fontSize: "20vw",
    fontWeight: "bold",
    textAlign: "center",
    height: "100%",
    width: "100%",
    "&:focus": {
      outline: "none",
    },
  },
  squareBox: {
    width: "33.3%",
    height: "33.3%",
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
    let json = { field: this.props.fieldNumber };
    fetch('/api/game/' + this.props.id + '/send-move/', {
      method: 'post',
      body: JSON.stringify(json)
    });
    this.props.callBack(this.props.fieldNumber);
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
      board: [0, 0, 0, 0, 0, 0, 0, 0, 0],
      result: ' ',
      width: null,
    };
    const { classes } = props;
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
    return <Square fieldNumber={i} value={this.state.board[i]}
                   callBack={this.squareClicked} id={this.props.id}/>;
  }

  squareClicked = (i) => {
    let b = this.state.board;
    if (b[i] === 0) {
      b[i] = 1;
      this.setState({board: b});
    }
  }
  
  updateBoard = () => {
    fetch('/api/game/' + this.props.id + '/get-move')
      .then((response) => response.json())
      .then(json => {
        this.setState({ board: json.state, result: json.result });
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
  });

  useEffect(() => {
    if (state.id === "") {
      startGame();
    }
  });

  function startGame() {
    fetch('/api/game/start-game')
      .then((response) => response.json())
      .then(json => {
        setState({ ...state, id: json.id });
      }).catch(() => {
        console.log("Error start game");
      });
  }   

  const classes = useStyles();

  return (
    <div>
      <Skeleton /> 
      <div className={classes.toolbar}/>
      <Container className={classes.myContainer}>
        <div className={classes.boardBox}>
          <Board id={state.id}/>
          <Button variant="contained" color="primary" className={classes.button} onClick={startGame}>
            Reset Game
          </Button>
          <Button variant="contained" color="secondary" className={classes.button} onClick={startGame}>
            Start Game
          </Button>
        </div>
      </Container>
    </div>
  );
}


