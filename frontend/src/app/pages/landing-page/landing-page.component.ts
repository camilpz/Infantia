import { Component } from '@angular/core';
import { FEATURES } from '../../models/models';

@Component({
  selector: 'app-landing-page',
  imports: [],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent {
  features = FEATURES;

}
