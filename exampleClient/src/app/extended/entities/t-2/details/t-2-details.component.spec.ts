import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { DetailsComponent, ListComponent, FieldsComp } from 'src/app/common/general-components';

import { TestingModule, EntryComponents } from 'src/testing/utils';
import { T2ExtendedService, T2DetailsExtendedComponent, T2ListExtendedComponent } from '../';
import { IT2 } from 'src/app/entities/t-2';
describe('T2DetailsExtendedComponent', () => {
  let component: T2DetailsExtendedComponent;
  let fixture: ComponentFixture<T2DetailsExtendedComponent>;
  let el: HTMLElement;
  
  describe('Unit Tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          T2DetailsExtendedComponent,
          DetailsComponent
        ],
        imports: [TestingModule],
        providers: [
          T2ExtendedService,
        ],
        schemas: [NO_ERRORS_SCHEMA]  
      }).compileComponents();
    }));
  
    beforeEach(() => {
      fixture = TestBed.createComponent(T2DetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

  });
  
  describe('Integration Tests', () => {
    beforeEach(async(() => {

      TestBed.configureTestingModule({
        declarations: [
          T2DetailsExtendedComponent,
          T2ListExtendedComponent,
          DetailsComponent,
          ListComponent,
          FieldsComp
        ].concat(EntryComponents),
        imports: [
          TestingModule,
          RouterTestingModule.withRoutes([
            { path: 't2', component: T2DetailsExtendedComponent },
            { path: 't2/:id', component: T2ListExtendedComponent }
          ])
        ],
        providers: [
          T2ExtendedService
        ]

      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(T2DetailsExtendedComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

  });
  
});
