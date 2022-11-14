import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IClient, NewClient } from '../client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClient for edit and NewClientFormGroupInput for create.
 */
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;

type ClientFormDefaults = Pick<NewClient, 'id' | 'products' | 'forfaits'>;

type ClientFormGroupContent = {
  id: FormControl<IClient['id'] | NewClient['id']>;
  accountId: FormControl<IClient['accountId']>;
  companyName: FormControl<IClient['companyName']>;
  firstName: FormControl<IClient['firstName']>;
  address: FormControl<IClient['address']>;
  phoneNumber: FormControl<IClient['phoneNumber']>;
  products: FormControl<IClient['products']>;
  forfaits: FormControl<IClient['forfaits']>;
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientFormService {
  createClientFormGroup(client: ClientFormGroupInput = { id: null }): ClientFormGroup {
    const clientRawValue = {
      ...this.getFormDefaults(),
      ...client,
    };
    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl(
        { value: clientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      accountId: new FormControl(clientRawValue.accountId),
      companyName: new FormControl(clientRawValue.companyName),
      firstName: new FormControl(clientRawValue.firstName),
      address: new FormControl(clientRawValue.address),
      phoneNumber: new FormControl(clientRawValue.phoneNumber),
      products: new FormControl(clientRawValue.products ?? []),
      forfaits: new FormControl(clientRawValue.forfaits ?? []),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return form.getRawValue() as IClient | NewClient;
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = { ...this.getFormDefaults(), ...client };
    form.reset(
      {
        ...clientRawValue,
        id: { value: clientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ClientFormDefaults {
    return {
      id: null,
      products: [],
      forfaits: [],
    };
  }
}
