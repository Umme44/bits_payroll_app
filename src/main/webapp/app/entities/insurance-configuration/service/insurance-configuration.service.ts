import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInsuranceConfiguration, NewInsuranceConfiguration } from '../insurance-configuration.model';

export type PartialUpdateInsuranceConfiguration = Partial<IInsuranceConfiguration> & Pick<IInsuranceConfiguration, 'id'>;

export type EntityResponseType = HttpResponse<IInsuranceConfiguration>;
export type EntityArrayResponseType = HttpResponse<IInsuranceConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class InsuranceConfigurationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/insurance-configurations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(insuranceConfiguration: NewInsuranceConfiguration): Observable<EntityResponseType> {
    return this.http.post<IInsuranceConfiguration>(this.resourceUrl, insuranceConfiguration, { observe: 'response' });
  }

  update(insuranceConfiguration: IInsuranceConfiguration): Observable<EntityResponseType> {
    return this.http.put<IInsuranceConfiguration>(`${this.resourceUrl}`, insuranceConfiguration, { observe: 'response' });
  }

  partialUpdate(insuranceConfiguration: PartialUpdateInsuranceConfiguration): Observable<EntityResponseType> {
    return this.http.patch<IInsuranceConfiguration>(
      `${this.resourceUrl}/${this.getInsuranceConfigurationIdentifier(insuranceConfiguration)}`,
      insuranceConfiguration,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInsuranceConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInsuranceConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInsuranceConfigurationIdentifier(insuranceConfiguration: Pick<IInsuranceConfiguration, 'id'>): number {
    return insuranceConfiguration.id;
  }

  compareInsuranceConfiguration(o1: Pick<IInsuranceConfiguration, 'id'> | null, o2: Pick<IInsuranceConfiguration, 'id'> | null): boolean {
    return o1 && o2 ? this.getInsuranceConfigurationIdentifier(o1) === this.getInsuranceConfigurationIdentifier(o2) : o1 === o2;
  }

  addInsuranceConfigurationToCollectionIfMissing<Type extends Pick<IInsuranceConfiguration, 'id'>>(
    insuranceConfigurationCollection: Type[],
    ...insuranceConfigurationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const insuranceConfigurations: Type[] = insuranceConfigurationsToCheck.filter(isPresent);
    if (insuranceConfigurations.length > 0) {
      const insuranceConfigurationCollectionIdentifiers = insuranceConfigurationCollection.map(
        insuranceConfigurationItem => this.getInsuranceConfigurationIdentifier(insuranceConfigurationItem)!
      );
      const insuranceConfigurationsToAdd = insuranceConfigurations.filter(insuranceConfigurationItem => {
        const insuranceConfigurationIdentifier = this.getInsuranceConfigurationIdentifier(insuranceConfigurationItem);
        if (insuranceConfigurationCollectionIdentifiers.includes(insuranceConfigurationIdentifier)) {
          return false;
        }
        insuranceConfigurationCollectionIdentifiers.push(insuranceConfigurationIdentifier);
        return true;
      });
      return [...insuranceConfigurationsToAdd, ...insuranceConfigurationCollection];
    }
    return insuranceConfigurationCollection;
  }
}
