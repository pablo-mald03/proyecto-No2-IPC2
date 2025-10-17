import { Component, Input } from '@angular/core';
import { Cine } from '../../../models/cines/cine';

@Component({
  selector: 'app-cines-card',
  imports: [],
  templateUrl: './cines-card.component.html',
  styleUrl: './cines-card.component.scss'
})
export class CinesCardComponent {

  @Input() cine!: Cine;

}
