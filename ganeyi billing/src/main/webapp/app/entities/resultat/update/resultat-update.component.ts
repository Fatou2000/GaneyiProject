import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ResultatFormService, ResultatFormGroup } from './resultat-form.service';
import { IResultat } from '../resultat.model';
import { ResultatService } from '../service/resultat.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-resultat-update',
  templateUrl: './resultat-update.component.html',
})
export class ResultatUpdateComponent implements OnInit {
  isSaving = false;
  resultat: IResultat | null = null;

  editForm: ResultatFormGroup = this.resultatFormService.createResultatFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected resultatService: ResultatService,
    protected resultatFormService: ResultatFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultat }) => {
      this.resultat = resultat;
      if (resultat) {
        this.updateForm(resultat);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('billingApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resultat = this.resultatFormService.getResultat(this.editForm);
    if (resultat.id !== null) {
      this.subscribeToSaveResponse(this.resultatService.update(resultat));
    } else {
      this.subscribeToSaveResponse(this.resultatService.create(resultat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResultat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(resultat: IResultat): void {
    this.resultat = resultat;
    this.resultatFormService.resetForm(this.editForm, resultat);
  }
}
