import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from "@angular/router";
import { HeaderAdminCineComponent } from "../../components/administrador-cine/header-admin-cine/header-admin-cine.component";
import { FooterComponent } from "../../components/footer/footer.component";

@Component({
  selector: 'app-admin-cine-page',
  imports: [RouterModule, RouterOutlet, HeaderAdminCineComponent, FooterComponent],
  templateUrl: './admin-cine-page.component.html',
  styleUrl: './admin-cine-page.component.scss'
})
export class AdminCinePageComponent {

}
