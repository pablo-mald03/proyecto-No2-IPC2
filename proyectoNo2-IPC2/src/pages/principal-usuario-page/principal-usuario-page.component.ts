import { Component } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { FooterComponent } from "../../components/footer/footer.component";

@Component({
  selector: 'app-principal-usuario-page',
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './principal-usuario-page.component.html',
  styleUrl: './principal-usuario-page.component.scss'
})
export class PrincipalUsuarioPageComponent {

}
