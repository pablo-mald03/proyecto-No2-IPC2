import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Master } from '../services/masterLog/master';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('proyectoNo2-IPC2');

}
