import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResultat, NewResultat } from '../resultat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResultat for edit and NewResultatFormGroupInput for create.
 */
type ResultatFormGroupInput = IResultat | PartialWithRequiredKeyOf<NewResultat>;

type ResultatFormDefaults = Pick<NewResultat, 'id'>;

type ResultatFormGroupContent = {
  id: FormControl<IResultat['id'] | NewResultat['id']>;
  value: FormControl<IResultat['value']>;
  valueContentType: FormControl<IResultat['valueContentType']>;
};

export type ResultatFormGroup = FormGroup<ResultatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResultatFormService {
  createResultatFormGroup(resultat: ResultatFormGroupInput = { id: null }): ResultatFormGroup {
    const resultatRawValue = {
      ...this.getFormDefaults(),
      ...resultat,
    };
    return new FormGroup<ResultatFormGroupContent>({
      id: new FormControl(
        { value: resultatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      value: new FormControl(resultatRawValue.value),
      valueContentType: new FormControl(resultatRawValue.valueContentType),
    });
  }

  getResultat(form: ResultatFormGroup): IResultat | NewResultat {
    return form.getRawValue() as IResultat | NewResultat;
  }

  resetForm(form: ResultatFormGroup, resultat: ResultatFormGroupInput): void {
    const resultatRawValue = { ...this.getFormDefaults(), ...resultat };
    form.reset(
      {
        ...resultatRawValue,
        id: { value: resultatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ResultatFormDefaults {
    return {
      id: null,
    };
  }
}
