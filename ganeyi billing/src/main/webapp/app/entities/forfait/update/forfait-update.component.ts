import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ForfaitFormService, ForfaitFormGroup } from './forfait-form.service';
import { IForfait } from '../forfait.model';
import { ForfaitService } from '../service/forfait.service';

@Component({
  selector: 'jhi-forfait-update',
  templateUrl: './forfait-update.component.html',
})
export class ForfaitUpdateComponent implements OnInit {
  isSaving = false;
  forfait: IForfait | null = null;

  editForm: ForfaitFormGroup = this.forfaitFormService.createForfaitFormGroup();

  constructor(
    protected forfaitService: ForfaitService,
    protected forfaitFormService: ForfaitFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forfait }) => {
      this.forfait = forfait;
      if (forfait) {
        this.updateForm(forfait);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const forfait = this.forfaitFormService.getForfait(this.editForm);
    if (forfait.id !== null) {
      this.subscribeToSaveResponse(this.forfaitService.update(forfait));
    } else {
      this.subscribeToSaveResponse(this.forfaitService.create(forfait));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IForfait>>): void {
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

  protected updateForm(forfait: IForfait): void {
    this.forfait = forfait;
    this.forfaitFormService.resetForm(this.editForm, forfait);
  }
}
