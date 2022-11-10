import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IForfait, NewForfait } from '../forfait.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IForfait for edit and NewForfaitFormGroupInput for create.
 */
type ForfaitFormGroupInput = IForfait | PartialWithRequiredKeyOf<NewForfait>;

type ForfaitFormDefaults = Pick<NewForfait, 'id' | 'actif' | 'clients'>;

type ForfaitFormGroupContent = {
  id: FormControl<IForfait['id'] | NewForfait['id']>;
  nom: FormControl<IForfait['nom']>;
  description: FormControl<IForfait['description']>;
  numberOfQueries: FormControl<IForfait['numberOfQueries']>;
  price: FormControl<IForfait['price']>;
  periode: FormControl<IForfait['periode']>;
  actif: FormControl<IForfait['actif']>;
  clients: FormControl<IForfait['clients']>;
};

export type ForfaitFormGroup = FormGroup<ForfaitFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ForfaitFormService {
  createForfaitFormGroup(forfait: ForfaitFormGroupInput = { id: null }): ForfaitFormGroup {
    const forfaitRawValue = {
      ...this.getFormDefaults(),
      ...forfait,
    };
    return new FormGroup<ForfaitFormGroupContent>({
      id: new FormControl(
        { value: forfaitRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nom: new FormControl(forfaitRawValue.nom),
      description: new FormControl(forfaitRawValue.description),
      numberOfQueries: new FormControl(forfaitRawValue.numberOfQueries),
      price: new FormControl(forfaitRawValue.price),
      periode: new FormControl(forfaitRawValue.periode),
      actif: new FormControl(forfaitRawValue.actif),
      clients: new FormControl(forfaitRawValue.clients ?? []),
    });
  }

  getForfait(form: ForfaitFormGroup): IForfait | NewForfait {
    return form.getRawValue() as IForfait | NewForfait;
  }

  resetForm(form: ForfaitFormGroup, forfait: ForfaitFormGroupInput): void {
    const forfaitRawValue = { ...this.getFormDefaults(), ...forfait };
    form.reset(
      {
        ...forfaitRawValue,
        id: { value: forfaitRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ForfaitFormDefaults {
    return {
      id: null,
      actif: false,
      clients: [],
    };
  }
}
