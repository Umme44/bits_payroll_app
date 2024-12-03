import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeePinConfiguration, NewEmployeePinConfiguration } from '../employee-pin-configuration.model';
import { EmployeeCategory } from '../../enumerations/employee-category.model';

export type PartialUpdateEmployeePinConfiguration = Partial<IEmployeePinConfiguration> & Pick<IEmployeePinConfiguration, 'id'>;

type RestOf<T extends IEmployeePinConfiguration | NewEmployeePinConfiguration> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestEmployeePinConfiguration = RestOf<IEmployeePinConfiguration>;

export type NewRestEmployeePinConfiguration = RestOf<NewEmployeePinConfiguration>;

export type PartialUpdateRestEmployeePinConfiguration = RestOf<PartialUpdateEmployeePinConfiguration>;

export type EntityResponseType = HttpResponse<IEmployeePinConfiguration>;
export type EntityArrayResponseType = HttpResponse<IEmployeePinConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class EmployeePinConfigurationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/employee-pin-configurations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeePinConfiguration: NewEmployeePinConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeePinConfiguration);
    return this.http
      .post<RestEmployeePinConfiguration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employeePinConfiguration: IEmployeePinConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeePinConfiguration);
    return this.http
      .put<RestEmployeePinConfiguration>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employeePinConfiguration: PartialUpdateEmployeePinConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeePinConfiguration);
    return this.http
      .patch<RestEmployeePinConfiguration>(
        `${this.resourceUrl}/${this.getEmployeePinConfigurationIdentifier(employeePinConfiguration)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeePinConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeePinConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeePinConfigurationIdentifier(employeePinConfiguration: Pick<IEmployeePinConfiguration, 'id'>): number {
    return employeePinConfiguration.id;
  }

  isPinSequenceUnique(req?: any): Observable<HttpResponse<boolean>> {
    const options = createRequestOption(req);
    return this.http.get<boolean>(`${this.resourceUrl}/is-pin-sequence-unique`, { params: options, observe: 'response' });
  }

  getEmployeePinConfigurationByEmployeeCategory(employeeCategory: EmployeeCategory): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ employeeCategory });
    return this.http
      .get<RestEmployeePinConfiguration[]>(this.resourceUrl + `/get-by-employee-category`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  compareEmployeePinConfiguration(
    o1: Pick<IEmployeePinConfiguration, 'id'> | null,
    o2: Pick<IEmployeePinConfiguration, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getEmployeePinConfigurationIdentifier(o1) === this.getEmployeePinConfigurationIdentifier(o2) : o1 === o2;
  }

  addEmployeePinConfigurationToCollectionIfMissing<Type extends Pick<IEmployeePinConfiguration, 'id'>>(
    employeePinConfigurationCollection: Type[],
    ...employeePinConfigurationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeePinConfigurations: Type[] = employeePinConfigurationsToCheck.filter(isPresent);
    if (employeePinConfigurations.length > 0) {
      const employeePinConfigurationCollectionIdentifiers = employeePinConfigurationCollection.map(
        employeePinConfigurationItem => this.getEmployeePinConfigurationIdentifier(employeePinConfigurationItem)!
      );
      const employeePinConfigurationsToAdd = employeePinConfigurations.filter(employeePinConfigurationItem => {
        const employeePinConfigurationIdentifier = this.getEmployeePinConfigurationIdentifier(employeePinConfigurationItem);
        if (employeePinConfigurationCollectionIdentifiers.includes(employeePinConfigurationIdentifier)) {
          return false;
        }
        employeePinConfigurationCollectionIdentifiers.push(employeePinConfigurationIdentifier);
        return true;
      });
      return [...employeePinConfigurationsToAdd, ...employeePinConfigurationCollection];
    }
    return employeePinConfigurationCollection;
  }

  protected convertDateFromClient<
    T extends IEmployeePinConfiguration | NewEmployeePinConfiguration | PartialUpdateEmployeePinConfiguration
  >(employeePinConfiguration: T): RestOf<T> {
    return {
      ...employeePinConfiguration,
      createdAt: employeePinConfiguration.createdAt?.toJSON() ?? null,
      updatedAt: employeePinConfiguration.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmployeePinConfiguration: RestEmployeePinConfiguration): IEmployeePinConfiguration {
    return {
      ...restEmployeePinConfiguration,
      createdAt: restEmployeePinConfiguration.createdAt ? dayjs(restEmployeePinConfiguration.createdAt) : undefined,
      updatedAt: restEmployeePinConfiguration.updatedAt ? dayjs(restEmployeePinConfiguration.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeePinConfiguration>): HttpResponse<IEmployeePinConfiguration> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeePinConfiguration[]>): HttpResponse<IEmployeePinConfiguration[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
