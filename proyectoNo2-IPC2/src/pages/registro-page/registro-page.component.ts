import { Component } from '@angular/core';
import { RouterLinkActive, RouterModule } from '@angular/router';
import { FormRegistroComponent } from "../../components/registro-usuario/form-registro-component/form-registro.component";

@Component({
  selector: 'app-registro-page.component',
  imports: [RouterModule, RouterLinkActive, FormRegistroComponent],
  templateUrl: './registro-page.component.html',
  styleUrl: './registro-page.component.scss'
})
export class RegistroPageComponent {

}
