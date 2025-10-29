import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { ReestablecerCredencialesComponent } from "../../components/registro-usuario/reestablecer-credenciales/reestablecer-credenciales.component";

@Component({
  selector: 'app-reestablecer-credenciales-page',
  imports: [RouterLink, RouterLinkActive, ReestablecerCredencialesComponent],
  templateUrl: './reestablecer-credenciales-page.component.html',
  styleUrl: './reestablecer-credenciales-page.component.scss'
})
export class ReestablecerCredencialesPageComponent {

}
