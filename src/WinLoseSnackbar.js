import React from 'react';
import Button from '@material-ui/core/Button';
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import { makeStyles } from '@material-ui/core/styles';
import { amber, green } from '@material-ui/core/colors';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import ErrorIcon from '@material-ui/icons/Error';
import InfoIcon from '@material-ui/icons/Info';
import CloseIcon from '@material-ui/icons/Close';
import clsx from 'clsx';

const PLAYER_X = "1";
const PLAYER_O = "2";
const DRAW = "0";

const useStyles = makeStyles(theme => ({
  success: {
    backgroundColor: green[600],
  },
  error: {
    backgroundColor: theme.palette.error.dark,
  },
  info: {
    backgroundColor: theme.palette.primary.main,
  },
  warning: {
    backgroundColor: amber[700],
  },
  icon: {
    fontSize: 20,
  },
  iconVariant: {
    opacity: 0.9,
    marginRight: theme.spacing(1),
  },
  message: {
    display: 'flex',
    alignItems: 'center',
  },
  margin: {
    margin: theme.spacing(1),
  }
}));

export default function WinLoseSnackbar(props) {
  const classes = useStyles();

  let vertical = 'bottom';
  let horizontal = 'center';

  let msg = "";
  let open = false;
  let Icon = InfoIcon;
  let variant = "info";
  if (props.result === PLAYER_X) { // l and w for lose and win are negated because
                              // it is sent from computer (opponents) perspective
    msg = "You have won the game";
    open = true;
    Icon = CheckCircleIcon;
    variant = "success";
  }
  if (props.result === PLAYER_O) {
    msg = "You have lost the game";
    open = true;
    Icon = ErrorIcon;
    variant = "error";
  }
  if (props.result === DRAW) {
    msg = "It's a draw";
    open = true;
    Icon = InfoIcon;
    variant = "info";
  }

  return (
    <div>
      <Snackbar
        anchorOrigin={{ vertical, horizontal }}
        key={`${vertical},${horizontal}`}
        open={open}
      >
        <SnackbarContent
          className={clsx(classes[variant], "classes.margin")}
          aria-describedby="client-snackbar"
          message={
            <span id="client-snackbar" className={classes.message}>
              <Icon className={clsx(classes.icon, classes.iconVariant)} />
              {msg}
            </span>
          }
        />
      </Snackbar>
    </div>
  );
}
