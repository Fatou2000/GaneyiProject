import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResultat } from '../resultat.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-resultat-detail',
  templateUrl: './resultat-detail.component.html',
})
export class ResultatDetailComponent implements OnInit {
  resultat: IResultat | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultat }) => {
      this.resultat = resultat;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
