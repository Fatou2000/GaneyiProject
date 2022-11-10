import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IForfait } from '../forfait.model';

@Component({
  selector: 'jhi-forfait-detail',
  templateUrl: './forfait-detail.component.html',
})
export class ForfaitDetailComponent implements OnInit {
  forfait: IForfait | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forfait }) => {
      this.forfait = forfait;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
