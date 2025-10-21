import { Component } from '@angular/core';
import { HeaderAnuncianteComponent } from "../../components/pagina-anunciante/header-anunciante/header-anunciante.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { RouterModule, RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-anunciante-page',
  imports: [HeaderAnuncianteComponent, FooterComponent, RouterModule, RouterOutlet],
  templateUrl: './anunciante-page.component.html',
  styleUrl: './anunciante-page.component.scss'
})
export class AnunciantePageComponent {

}
