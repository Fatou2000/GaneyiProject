import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductLicense, NewProductLicense } from '../product-license.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductLicense for edit and NewProductLicenseFormGroupInput for create.
 */
type ProductLicenseFormGroupInput = IProductLicense | PartialWithRequiredKeyOf<NewProductLicense>;

type ProductLicenseFormDefaults = Pick<NewProductLicense, 'id' | 'isActive'>;

type ProductLicenseFormGroupContent = {
  id: FormControl<IProductLicense['id'] | NewProductLicense['id']>;
  accessKey: FormControl<IProductLicense['accessKey']>;
  startDate: FormControl<IProductLicense['startDate']>;
  endDate: FormControl<IProductLicense['endDate']>;
  isActive: FormControl<IProductLicense['isActive']>;
};

export type ProductLicenseFormGroup = FormGroup<ProductLicenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductLicenseFormService {
  createProductLicenseFormGroup(productLicense: ProductLicenseFormGroupInput = { id: null }): ProductLicenseFormGroup {
    const productLicenseRawValue = {
      ...this.getFormDefaults(),
      ...productLicense,
    };
    return new FormGroup<ProductLicenseFormGroupContent>({
      id: new FormControl(
        { value: productLicenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      accessKey: new FormControl(productLicenseRawValue.accessKey),
      startDate: new FormControl(productLicenseRawValue.startDate),
      endDate: new FormControl(productLicenseRawValue.endDate),
      isActive: new FormControl(productLicenseRawValue.isActive),
    });
  }

  getProductLicense(form: ProductLicenseFormGroup): IProductLicense | NewProductLicense {
    return form.getRawValue() as IProductLicense | NewProductLicense;
  }

  resetForm(form: ProductLicenseFormGroup, productLicense: ProductLicenseFormGroupInput): void {
    const productLicenseRawValue = { ...this.getFormDefaults(), ...productLicense };
    form.reset(
      {
        ...productLicenseRawValue,
        id: { value: productLicenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductLicenseFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
