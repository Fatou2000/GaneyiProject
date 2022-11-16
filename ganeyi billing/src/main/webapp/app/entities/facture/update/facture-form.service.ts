import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFacture, NewFacture } from '../facture.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacture for edit and NewFactureFormGroupInput for create.
 */
type FactureFormGroupInput = IFacture | PartialWithRequiredKeyOf<NewFacture>;

type FactureFormDefaults = Pick<NewFacture, 'id' | 'manytomanies' | 'products'>;

type FactureFormGroupContent = {
  id: FormControl<IFacture['id'] | NewFacture['id']>;
  rabais: FormControl<IFacture['rabais']>;
  tva: FormControl<IFacture['tva']>;
  sousTotal: FormControl<IFacture['sousTotal']>;
  total: FormControl<IFacture['total']>;
  typeFacturation: FormControl<IFacture['typeFacturation']>;
  status: FormControl<IFacture['status']>;
  reference: FormControl<IFacture['reference']>;
  date: FormControl<IFacture['date']>;
  numero: FormControl<IFacture['numero']>;
  forfait: FormControl<IFacture['forfait']>;
  client: FormControl<IFacture['client']>;
  manytomanies: FormControl<IFacture['manytomanies']>;
  products: FormControl<IFacture['products']>;
};

export type FactureFormGroup = FormGroup<FactureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactureFormService {
  createFactureFormGroup(facture: FactureFormGroupInput = { id: null }): FactureFormGroup {
    const factureRawValue = {
      ...this.getFormDefaults(),
      ...facture,
    };
    return new FormGroup<FactureFormGroupContent>({
      id: new FormControl(
        { value: factureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rabais: new FormControl(factureRawValue.rabais),
      tva: new FormControl(factureRawValue.tva),
      sousTotal: new FormControl(factureRawValue.sousTotal),
      total: new FormControl(factureRawValue.total),
      typeFacturation: new FormControl(factureRawValue.typeFacturation),
      status: new FormControl(factureRawValue.status),
      reference: new FormControl(factureRawValue.reference),
      date: new FormControl(factureRawValue.date),
      numero: new FormControl(factureRawValue.numero),
      forfait: new FormControl(factureRawValue.forfait),
      client: new FormControl(factureRawValue.client),
      manytomanies: new FormControl(factureRawValue.manytomanies ?? []),
      products: new FormControl(factureRawValue.products ?? []),
    });
  }

  getFacture(form: FactureFormGroup): IFacture | NewFacture {
    return form.getRawValue() as IFacture | NewFacture;
  }

  resetForm(form: FactureFormGroup, facture: FactureFormGroupInput): void {
    const factureRawValue = { ...this.getFormDefaults(), ...facture };
    form.reset(
      {
        ...factureRawValue,
        id: { value: factureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FactureFormDefaults {
    return {
      id: null,
      manytomanies: [],
      products: [],
    };
  }
}
